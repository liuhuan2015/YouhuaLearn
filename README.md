# YouhuaLearn
android性能优化总结<br>
## android ui性能优化
今天看了鸿阳大神的一篇关于android ui性能优化的播客，记录一下。博文地址：http://blog.csdn.net/lmj623565791/article/details/45556391/。
文中提到android系统每隔16ms会发出VSYNC信号，触发对ui进行渲染，如果ui渲染的整个过程在16ms内完成，则界面就不会卡顿。如果超出16ms，就会造成丢帧，
logcat中会有drop frames的警告，界面会产生卡顿现象。<br>
在android ui性能优化方面主要有两个方向：<br>
1，布局文件中要避免不必要的嵌套<br>
2，避免界面overdraw。<br>
设置-->开发者选项-->调试GPU过度绘制-->显示GPU过度绘制，当我们启动这个设置后，就可以看到我们的界面那里过度绘制了。<br>
对于overdraw，有两个解决方法：1，移除不必要的background；2，使用android的Canvas对象给我们提供的clipRect方法。<br>
这个工程即是使用android的Canvas对象给我们提供的clipRect方法解决overdraw的代码实现。<br>

部分代码：<br>

      @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(20, 120);
        for (int i = 0; i < mCards.length; i++) {
            canvas.translate(120, 0);
            canvas.save();
            if (i < mCards.length - 1) {
                canvas.clipRect(0, 0, 120, mCards[i].getHeight());
            }
            canvas.drawBitmap(mCards[i], 0, 0, null);
            canvas.restore();
        }
        canvas.restore();
    }
    
## android代码性能优化
上面写的是android ui的性能优化，这里就歇一歇开发时代码上的一些性能优化措施。<br>
1,在一个类中避免创建过多的对象，因为每一个对象在创建时都需要开辟内存空间，同时在一个类中我们要适当的使用软引用，弱引用，虚引用（这个感觉从来没用过），
这样的话在有些变量不再使用的时候，能及时释放掉其占用的内存空间。<br>
强引用即类的成员变量，它的生命周期跟随所在类，所在类被销毁了，它才会被销毁。<br>
软引用(SoftReference)在内存不足时会被回收,JVM会根据当前堆的使用情况来判断何时回收。当堆的使用率临近阈值时，才会回收软引用的对象<br>
弱引用是一种比软引用较弱的引用类型。在系统GC时，只要发现弱引用，不管系统堆空间是否足够，都会将对象进行回收。<br>

2,在数据结构方面，不要过多使用枚举，因为枚举占用的内存空间要比整形大。多使用一些android特有的数据结构，比如SparseArray。<br>
虽说SparseArray性能比较好，但是由于其添加、查找、删除数据都需要先进行一次二分查找，所以在数据量大的情况下性能并不明显，将降低至少50%。<br>
满足下面两个条件我们可以使用SparseArray代替HashMap：<br>

    数据量不大，最好在千级以内。
    key必须为int类型，这中情况下的HashMap可以用SparseArray代替。
  
SparseArray比HashMap更省内存，在某些条件下性能更好，主要是因为它避免了对key的自动装箱（int转为Integer类型），它内部则是通过两个数组来进行数据存储的，<br>
一个存储key，另外一个存储value，为了优化性能，它内部对数据还采取了压缩的方式来表示稀疏数组的数据，从而节约内存空间，我们从源码中可以看到key和value<br>
分别是用数组表示：<br>

    private int[] mKeys;
    private Object[] mValues;

3，采用内存缓存和磁盘缓存(lrucache,DiskLrucache).


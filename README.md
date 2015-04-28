# AnimatedVectorDrawable Usage

![header](header.png)

## 前言
Android 5.0中提供了新的Drawable类型VectorDrawable、AnimatedVectorDrawable。VectorDrawable是Android对SVG格式的支持。AnimatedVectorDrawable算是对PropertyAnimation的一个应用。

## VectorDrawable
不同于之前的ShapeDrawable，SDK中仅仅提供有有限的图形支持，如矩形，圆形，圆角等，很多时候要实现一些效果就比较难受了，如ActionBar上Tab指示器，上部分透明下面部分提供一条颜色条做指示，如果动手做过就知道这块没有特别好的实现方式，基本上是通过两个矩形的叠加，然后上层矩形设置padding实现，如下：

```
<?xml version=“1.0” encoding=“utf-8”?>
<layer-list xmlns:android=“http://schemas.android.com/apk/res/android” >
	<item>
		<shape  android:shape=“rectangle”>
		    <solid android:color=“@color/light_mask” />
			<padding android:bottom=“2dp” />
		</shape>
	</item>
	<item>
		<shape android:shape=“rectangle”>
		    <solid android:color=“@color/main_color_transparent” />
		</shape>
	</item>
</layer-list>
```
SDK-21中新增的VectorDrawable支持了SVG就相当强大了，上面的效果可以这样实现了：

```
<vector
    xmlns:android=“http://schemas.android.com/apk/res/android”
    android:height=“@dimen/icon_size”
    android:width=“@dimen/icon_size”
    android:viewportHeight=“100”
    android:viewportWidth=“100” >

    <path
        android:name=“v”
        android:strokeColor=“@color/main_color_transparent”
        android:strokeWidth=“2dp”
        android:pathData=“M0,99L99,99”/>
</vector>
```
VectorDrawable可以看作是提供了一个Canvas，然后再Canvas上绘图的功能。Canvas的大小由viewPortHeight、viewportWidth指定，而绘图指令由pathData等属性指定。name属性制定了，该path的名字，注意这块将会在AnimatedVectorDrawable用到。

## PathData
官方说明上指出pathData和SVG格式中的d属性一致。这就简单了，下载一些SVG文件，打开来，将d属性拷贝出来放倒strings里面，然后给pathData指定就行了。为了方便手写一些简单的图形，这里将格式做一个简单的说明。格式如下：

```
(<op> <value-x>,<value-y>)+
```
其中op对应Canvas绘图的指令的缩写，value-x, value-y分别是指令的(x，y)坐标。

Canvas指令 |  op
———|————
moveTo | M
lineTo | L
curve | C
arc | A
close | z

那么我们在100, 100这个正方形中绘制一个等腰三角形的代码：

```
moveTo 50, 0
lineTo 100, 100
lineto 0, 100
close
```

就转换为``` M50,0L100,100L0,100z ```

## AnimatedVectorDrawable
AnimatedVectorDrawable可以看作是PropertyAnimation在VectorDrawable上的应用，为什么这样说呢，看例子：

```
<animated-vector xmlns:android=“http://schemas.android.com/apk/res/android”
    android:drawable=“@drawable/check” >
    <target
        android:name=“v”
        android:animation=“@anim/path_morph” />
</animated-vector>
```

这是一个典型的AnimatedVectorDrawable的定义，其中drawable属性制定了我们要进行动画的drawable名称，target指定了动画的目标，这里的name和我们在VectorDrawable中提到的name就对应了，animation制定了要进行的动画。连贯起来就是在check这个Drawable上的名字为v的path制定path_morph这个动画。

path_morph就简单了，一个明显的PropertyAnimation。如下：

```
<?xml version=“1.0” encoding=“utf-8”?>
<set xmlns:android=“http://schemas.android.com/apk/res/android”>
    <objectAnimator
        android:duration=“500”
        android:propertyName=“pathData”
        android:valueFrom=“@string/check”
        android:valueTo=“@string/equals”
        android:valueType=“pathType” />
</set>
```

指定了动画的时常，要修改的propertyName及起始终点值。这里修改的是pathData，如你所想，系统将根据pathData制定的数据创建合适的动画，不如你所想的是复杂了可不行。

## Can’t morph from xxx to xxxx
*这可能是限制了，如果想用AnimatedVectorDrawable，pathData中的指令序列必须完全一致。数据可以不一样。*

## 例子，创建从0－2数字的VectorDrawable，然后动起来！
代码见[github](https://github.com/galilio/AnimatedVectorDrawableSample)

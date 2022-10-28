package com.bard.gplearning

import android.animation.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bard.gplearning.widgets.hencoder.ProvinceEvaluator
import kotlinx.android.synthetic.main.activity_anim.*


class AnimActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)


        //ObjectAnimator演示
//        val animator = ObjectAnimator.ofFloat(view, "radius", 150.dp)
//        //需要重绘，在设置radius时invalidate()
//        animator.startDelay = 1000
//        animator.start()





        //AnimatorSet演示
//        val bottomFlipAnimator = ObjectAnimator.ofFloat(view, "bottomFlip", 60f)
//        bottomFlipAnimator.startDelay = 1000
//        bottomFlipAnimator.duration = 1000
//        //bottomFlipAnimator.start()
//
//        val flipRotationAnimator = ObjectAnimator.ofFloat(view, "flipRotation", 270f)
//        flipRotationAnimator.startDelay = 200
//        flipRotationAnimator.duration = 1000
//        //flipRotationAnimator.start()
//
//        val topFlipAnimator = ObjectAnimator.ofFloat(view, "topFlip", -60f)
//        topFlipAnimator.startDelay = 200
//        topFlipAnimator.duration = 1000
//        //topFlipAnimator.start()
//
//        //合为一个AnimatorSet
//        val animatorSet = AnimatorSet()
//        animatorSet.playSequentially(bottomFlipAnimator, flipRotationAnimator, topFlipAnimator)
//        animatorSet.start()






        //PropertyValuesHolder演示
//        val bottomFlipHolder = PropertyValuesHolder.ofFloat("bottomFlip", 60f)
//        val flipRotationHolder = PropertyValuesHolder.ofFloat("flipRotation", 270f)
//        val topFlipHolder = PropertyValuesHolder.ofFloat("topFlip", -60f)
//        //合为一个
//        val holderAnimator = ObjectAnimator.ofPropertyValuesHolder(view, bottomFlipHolder, flipRotationHolder, topFlipHolder)
//        holderAnimator.startDelay = 1000
//        holderAnimator.duration = 2000
//        holderAnimator.start()





        //Keyframe演示
//        val length = 200.dp
//        val keyframe1 = Keyframe.ofFloat(0f, 0f)
//        val keyframe2 = Keyframe.ofFloat(0.2f, 0.4f * length)
//        val keyframe3 = Keyframe.ofFloat(0.8f, 0.6f * length)
//        val keyframe4 = Keyframe.ofFloat(1f, 1f * length)
//
//        val keyframeHolder = PropertyValuesHolder.ofKeyframe("translationX",
//            keyframe1, keyframe2, keyframe3, keyframe4)
//        val animator = ObjectAnimator.ofPropertyValuesHolder(view, keyframeHolder)
//        animator.startDelay = 1000
//        animator.duration = 2000
//        animator.start()




        //Interpolator演示
//        val animator = ObjectAnimator.ofFloat()
//        // AcclerateDeceletateInterpolator() //先加速再减速，适用于变换场景，不适用于入场出场
//        // AcclerateInterpolator() //加速，适用于出场
//        // DecelerateInterpolator() //减速，适用于入场
//        // LinearInterpolator() //匀速
//        animator.interpolator = LinearInterpolator()
//        animator.duration = 2000
//        animator.start()



        //TypeEvaluator演示
//        val animator = ObjectAnimator.ofObject(point, "point",
//            MyPointFEvaluator(), PointF(100.dp, 200.dp))
//        animator.startDelay = 1000
//        animator.duration = 2000
//        animator.start()




        //字符串动画【适用抽奖】 演示
        val animator = ObjectAnimator.ofObject(textview, "province", ProvinceEvaluator(), "台湾省")
        animator.startDelay = 1000
        animator.duration = 2000
        animator.start()
    }

}
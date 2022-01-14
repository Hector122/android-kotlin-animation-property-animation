/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.animation.addListener


class MainActivity : AppCompatActivity() {
    
    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)
        
        rotateButton.setOnClickListener {
            rotater()
        }
        
        translateButton.setOnClickListener {
            translater()
        }
        
        scaleButton.setOnClickListener {
            scaler()
        }
        
        fadeButton.setOnClickListener {
            fader()
        }
        
        colorizeButton.setOnClickListener {
            colorizer()
        }
        
        showerButton.setOnClickListener {
            shower()
        }
    }
    
    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
        animator.duration = 1000 //300 milliseconds is a decent default for most animations
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()
    }
    
    private fun translater() {
        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        animator.repeatCount = 1 //which controls how many times it repeats after the first run
        animator.repeatMode = ObjectAnimator.REVERSE //For reversing the direction every time it repeats
        animator.disableViewDuringAnimation(translateButton)
        animator.start()
    }
    
    private fun scaler() {
        //. This time, you’re going to animate two properties in parallel.
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        
        //An ObjectAnimator can hold multiple PropertyValuesHolder objects,
        // which will all animate together, in parallel, when the ObjectAnimator starts.
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX, scaleY)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }
    
    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }
    
    private fun colorizer() {
        //var animator = ObjectAnimator.ofInt(star.parent, "backgroundColor", Color.BLACK, Color.RED ).start()
    
        //Animate colors, not integers
        var animator = ObjectAnimator.ofArgb(star.parent, "backgroundColor", Color.BLACK, Color.RED )
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }
    
    private fun shower() {
        val container = star.parent as ViewGroup
        val containerH = container.height
        val containerW = container.width
        var starW = star.width.toFloat()
        var starH = star.height.toFloat()
    
        //Create the star and add it to the background container.
        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams  = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
        
        container.addView(newStar)
    
        //scale
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY
    
        //translate
        newStar.translationX = Math.random().toFloat() * containerW - starW / 2
    
        //animators for star rotation and falling
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y,
                -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(1f)
        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
                (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()
        
        //AnimatorSet is basically a group of animations, along with instructions on when
        // to run those animations. It can play animations in parallel, as you will do here,
        // or sequentially (like you might do in the list-fading example mentioned earlier,
        // where you first fade out a view and then animate the resulting gap closed
        
        //Running the animations in parallel with AnimatorSet
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()
    
        //Use animation listeners to remove a view when the animation to move it off the screen is complete.
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })
        set.start()
    }
    
    private fun ObjectAnimator.disableViewDuringAnimation(view: View) {
        this.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                view.isEnabled = false
            }
            
            override fun onAnimationEnd(animation: Animator?) {
                view.isEnabled = true
            }
        })
    }
    
}

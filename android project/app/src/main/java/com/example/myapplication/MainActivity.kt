package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.lang.Math.abs
import java.util.*


class MainActivity : AppCompatActivity() {


        // 스톱워치

        var p_num = 3 //참가자 수
        var k = 1  // 참가자번호
        val point_list = mutableListOf<Float>() //점수저장리스트
        var isBlind = false


        fun start() {
            setContentView(R.layout.activity_start)

            val tv_pnum: TextView = findViewById(R.id.tv_pnum) as TextView
            val btn_plus: Button = findViewById(R.id.btn_plus) as Button
            val btn_minus: Button = findViewById(R.id.btn_minus) as Button
            val btn_start: Button = findViewById(R.id.btn_start) as Button
            val btn_blind: Button = findViewById(R.id.btn_blind) as Button

            tv_pnum.text = p_num.toString()

            btn_minus.setOnClickListener {
            p_num--
            if (p_num == 0) {
                p_num = 1
            }
            tv_pnum.text = p_num.toString()
            }

            btn_plus.setOnClickListener {
            p_num++
            tv_pnum.text = p_num.toString()
            }

            btn_start.setOnClickListener {
            main()
            }

            btn_blind.setOnClickListener {
                isBlind = !isBlind
                if (isBlind == true){
                    btn_blind.text = "Blind 모드 ON"
                } else{
                    btn_blind.text = "Blind 모드 OFF"
                }
            }

        }


        fun main() {

            setContentView(R.layout.activity_main)  // xml 화면뷰를 연결한다

            var timerTask: Timer? = null
            var stage = 1
            var sec: Int = 0

            val tv: TextView = findViewById(R.id.tv_random) as TextView
            val tv_t: TextView = findViewById(R.id.tv_timer) as TextView
            val tv_p: TextView = findViewById(R.id.tv_point) as TextView
            val tv_people: TextView = findViewById(R.id.tv_people) as TextView
            val btn: Button = findViewById(R.id.btn_operator) as Button
            val btn_i: Button = findViewById(R.id.btn_i) as Button
            val random = Random()
            val num = random.nextInt(1001)


            tv.text = ((num.toFloat()) / 100).toString()
            btn.text = "시작"
            tv_people.text = "참가자 $k"

            btn_i.setOnClickListener {
                point_list.clear()
                k = 1
                start()
            }

            btn.setOnClickListener {
                stage++
                if (stage == 2) {
                    timerTask = kotlin.concurrent.timer(period = 10) {
                        sec++
                        runOnUiThread {
                            if(isBlind == false) {
                                tv_t.text = (sec.toFloat() / 100).toString()
                            } else if(isBlind == true && stage ==2){
                                tv_t.text= "???"
                            }
                        }
                    }
                    btn.text = "정지"
                } else if (stage == 3) {
                    tv_t.text = (sec.toFloat() / 100).toString()
                    timerTask?.cancel()
                    val point = abs(sec - num).toFloat() / 100
                    point_list.add(point)   // 점수저장하기
                    tv_p.text = point.toString()
                    btn.text = "다음"
                    stage = 0
                } else if (stage == 1) {
                    if (k < p_num) {
                        k++
                        main()
                    } else {
                        end()
                    }

                }

            }

        }


        fun end() {
            setContentView(R.layout.activity_end)

            val tv_last: TextView = findViewById(R.id.tv_last) as TextView
            val tv_lpoint: TextView = findViewById(R.id.tv_lpoint) as TextView
            val btn_init: Button = findViewById(R.id.btn_init) as Button

            tv_lpoint.text = (point_list.maxOrNull()).toString() // 숫자가 큰 결과출력
            var index_last = point_list.indexOf(point_list.maxOrNull())
            tv_last.text = "참가자" + (index_last + 1).toString() // 숫자가 큰 사람출력

            btn_init.setOnClickListener {
                point_list.clear()
                k = 1
                start()
            }

        }


        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            start()

        }
    }

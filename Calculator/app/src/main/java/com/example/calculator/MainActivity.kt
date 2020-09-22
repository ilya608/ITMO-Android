package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Math.abs

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        number0.setOnClickListener { appendOnExpression("0"); }
        number1.setOnClickListener { appendOnExpression("1"); }
        number2.setOnClickListener { appendOnExpression("2"); }
        number3.setOnClickListener { appendOnExpression("3"); }
        number4.setOnClickListener { appendOnExpression("4"); }
        number5.setOnClickListener { appendOnExpression("5"); }
        number6.setOnClickListener { appendOnExpression("6"); }
        number7.setOnClickListener { appendOnExpression("7"); }
        number8.setOnClickListener { appendOnExpression("8"); }
        number9.setOnClickListener { appendOnExpression("9"); }
        sum.setOnClickListener { appendOnExpression("+"); }
        subtract.setOnClickListener { appendOnExpression("-"); }
        multiply.setOnClickListener { appendOnExpression("*"); }
        divide.setOnClickListener { appendOnExpression("/"); }
        comma.setOnClickListener { (appendOnExpression(".")) };
        compute.setOnClickListener { computeExpression() }
        cleen.setOnClickListener {
            text.setText("=");
        }
    }

    override fun onSaveInstanceState(outState : Bundle) {
        outState?.run {
            putString("text", text.text.toString());
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState);
        text.text = savedInstanceState?.getString("text");
    }

    fun computeExpression() {
        val EPS = 1e-6;
        try {
            val expression = text.text.toString().substring(1);
            Log.d("Пример", expression.toString());
            val evalExpression = ExpressionBuilder(expression).build().evaluate();
            var res = evalExpression.toString();
            if (abs(evalExpression.toInt() - evalExpression) < EPS) {
                res = evalExpression.toInt().toString();
            }
            text.text = "=$res";
        } catch (e: Exception) {
            text.text = "ERROR";
            Log.d("Ошибка", e.message.toString());
        }
    }

    fun appendOnExpression(string: String) {
        text.append(string);
    }
}

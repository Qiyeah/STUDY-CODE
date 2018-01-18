# Kotlin 基础语法
---
Kotlin 文件以 .kt 为后缀。

## 包声明

代码文件的开头一般为包的声明：

	package com.runoob.main
	
	import java.util.*
	
	fun test() {}

	class Runoob {}

kotlin源文件不需要相匹配的目录和包，源文件可以放在任何文件目录。

以上例中 test() 的全名是 com.runoob.main.test、Runoob 的全名是 com.runoob.main.Runoob。

如果没有指定包，默认为 default 包。

## 默认导入

有多个包会默认导入到每个 Kotlin 文件中：

	kotlin.*
	kotlin.annotation.*
	kotlin.collections.*
	kotlin.comparisons.*
	kotlin.io.*
	kotlin.ranges.*
	kotlin.sequences.*
	kotlin.text.*


##	函数定义

函数定义使用关键字 fun，参数格式为：参数 : 类型

	fun sum(a: Int, b: Int): Int {   // Int 参数，返回值 Int
	    return a + b
	}

表达式作为函数体，返回类型自动推断：

	fun sum(a: Int, b: Int) = a + b
	
	public fun sum(a: Int, b: Int): Int = a + b   // public 方法则必须明确写出返回类型

无返回值的函数(类似Java中的void)：

	fun printSum(a: Int, b: Int): Unit { 
	    print(a + b)
	}




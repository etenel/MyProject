package com.mvvm.demo.http

import com.mvvm.demo.entity.BaseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class RetrofitDSL<T> {
    internal lateinit var load: (suspend () -> BaseData<T?>)
        private set
    internal lateinit var onSuccess: ((T) -> Unit)
        private set
    internal var onEmpty: ((msg: String, code: Int) -> Unit)? = null
        private set
    internal var onFailed: ((msg: String?, code: Int) -> Unit)? = null
        private set
    internal var onComplete: (() -> Unit)? = null
        private set

    /**
     * 获取数据
     * @param block (T) -> Unit
     */
    fun load(block: suspend () -> BaseData<T?>) {
        this.load = block
    }

    /**
     * 获取数据成功
     * @param block (T) -> Unit
     */
    fun onSuccess(block: (T) -> Unit) {
        this.onSuccess = block
    }

    /**
     * 获取数据为空
     * @param block (T) -> Unit
     */
    fun onEmpty(block: (msg: String, code: Int) -> Unit) {
        this.onEmpty = block
    }

    /**
     * 获取数据失败
     * @param block (msg: String, errorCode: Int) -> Unit
     */
    fun onFailed(block: (msg: String?, code: Int) -> Unit) {
        this.onFailed = block
    }

    /**
     * 访问完成
     * @param block () -> Unit
     */
    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

}

//扩展函数


fun <T> CoroutineScope.retrofit(
    dsl: RetrofitDSL<T>.() -> Unit
) {
    launch {
        val retrofitDsl = RetrofitDSL<T>()
        retrofitDsl.dsl()
        try {
            val result = retrofitDsl.load()
            if (result.data != null) {
                retrofitDsl.onSuccess.invoke(result.data)
            } else {
                retrofitDsl.onEmpty?.invoke("数据为空", 0)
//                when (val code = result.code) {
//                    200 -> retrofitDsl.onSuccess.invoke(result)
//                    // 其他响应码
//                    else -> retrofitDsl.onFailed?.invoke(result.msg, code)
//                }
            }
        } catch (e: Exception) {
            retrofitDsl.onFailed?.invoke(e.message, -10)
        }
        // 其他异常类型
        finally {
            retrofitDsl.onComplete?.invoke()
        }
    }
}

sealed interface ResultState<out T> {
    data class Success<T>(val data: T) : ResultState<T>
    data class Error(val exception: Throwable? = null) : ResultState<Nothing>
    data class Empty(val msg: String = "") : ResultState<Nothing>
    data object Loading : ResultState<Nothing>

}

fun <T> Flow<BaseData<T>>.asResult(): Flow<ResultState<T>> {
    return this.map<BaseData<T>, ResultState<T>> {
        if (it.data != null) {
            ResultState.Success(it.data)
        } else {
            ResultState.Empty(it.msg)
        }
    }
        .onStart { emit(ResultState.Loading) }
        .catch {
            emit(ResultState.Error(it)) }

}
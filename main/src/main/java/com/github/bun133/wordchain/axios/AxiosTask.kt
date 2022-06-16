package com.github.bun133.wordchain.axios

import com.github.bun133.tinked.Task
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

class AxiosTask(val url: String, val parameters: Parameters) : Task<Unit, GooResponse?>() {
    override fun runnable(input: Unit): GooResponse {
        throw UnsupportedOperationException("not implemented")
    }

    override fun run(input: Unit) {
        val r = url
            .httpPost(parameters)
            .responseObject<GooResponse>(gsonDeserializer()) { _, res, result ->
                when (result) {
                    is Result.Success -> {
                        println("Success")
                        nextNode?.run(result.value)
                    }
                    is Result.Failure -> {
                        nextNode?.run(null)
                    }
                }
            }
    }
}
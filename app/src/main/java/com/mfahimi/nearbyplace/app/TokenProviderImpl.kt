package com.mfahimi.nearbyplace.app

class TokenProviderImpl(private val mClientId: String, private val mSecretId: String) :
    TokenProvider {
    override val clientId: String
        get() = mClientId
    override val secretId: String
        get() = mSecretId
}
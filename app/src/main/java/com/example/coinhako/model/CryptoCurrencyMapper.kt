package com.example.coinhako.model

/**
 * Mapper class to map data from entity to project's model
 * @author longtran
 * @since 29/05/2021
 */
object CryptoCurrencyMapper {
    fun transform(entity: CryptoCurrencyEntity?): CryptoCurrency {
        return CryptoCurrency(
            entity?.base,
            entity?.counter,
            entity?.buyPrice,
            entity?.sellPrice,
            entity?.icon,
            entity?.name,
        )
    }

    fun transformCollection(collection: List<CryptoCurrencyEntity?>?): ArrayList<CryptoCurrency> {
        val cardList = arrayListOf<CryptoCurrency>()
        if (collection != null) {
            for (entity in collection) {
                cardList.add(transform(entity))
            }
        }
        return cardList
    }
}
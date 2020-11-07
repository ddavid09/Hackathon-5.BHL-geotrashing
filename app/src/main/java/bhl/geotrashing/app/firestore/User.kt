package bhl.geotrashing.app.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
data class User(
    var userID: String = "brak",
    var nickname: String = "brak",
    var points: Int = 0

)


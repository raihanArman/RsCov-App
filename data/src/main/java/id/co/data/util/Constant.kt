package id.co.data.util

object Constant {
    const val BASE_URL = "http://192.168.43.22/api_rscov/"
}

object AppLink {
    object DetailHospital {
        const val HOSPITAL_DETAIL = "rscov://detail/{hospital_id}"
        const val PARAM_HOSPITAL_ID = "hospital_id"
    }

    object Home {
        const val HOME_LINK = "rscov://home"
    }

    object ListHospital {
        const val LIST_HOSPITAL_LINK = "rscov://list"
    }

}
package data.core.remote.service.image

import data.core.remote.service.base.BaseServiceImpl
import io.ktor.client.HttpClient

class ImageServiceImpl(
    client: HttpClient,
    baseHost: String
): BaseServiceImpl(client, baseHost), ImageService {



}
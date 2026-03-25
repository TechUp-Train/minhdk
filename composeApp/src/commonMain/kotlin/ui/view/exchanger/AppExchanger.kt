package ui.view.exchanger

import com.example.aigenerator.PlatformImage
import kotlinx.coroutines.channels.Channel

object AppExchanger {

    val exchangePickImageToMainPickImages = Channel<List<PlatformImage>>()

}
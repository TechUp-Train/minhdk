import SwiftUI

@main
struct iOSApp: App {

    init() {
        PlatformKt.initIosDependencies()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
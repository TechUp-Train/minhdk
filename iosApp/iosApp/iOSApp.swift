import SwiftUI

@main
struct iOSApp: App {

    init() {
        PlatformKt.initDependencies()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
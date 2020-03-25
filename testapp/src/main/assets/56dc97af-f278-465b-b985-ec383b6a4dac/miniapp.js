import { OS } from './dataType.js';
export var MiniApp;
(function (MiniApp) {
    class MiniAppImpl {
        getOS() {
            if (window.navigator.userAgent.match(/Android/i)) {
                return OS.Android;
            }
            else if (window.navigator.userAgent.match(/iPhone|iPad|iPod/i)) {
                return OS.iOS;
            }
            else {
                return OS.Unknown;
            }
        }
        getUniqueId() {
            if (this.getOS() === OS.iOS) {
                return window.webkit.messageHandlers.MiniApp.getUniqueId();
            }
            else {
                return window.MiniApp.getUniqueId();
            }
        }
    }
    MiniApp.MiniAppImpl = MiniAppImpl;
})(MiniApp || (MiniApp = {}));
//# sourceMappingURL=miniapp.js.map
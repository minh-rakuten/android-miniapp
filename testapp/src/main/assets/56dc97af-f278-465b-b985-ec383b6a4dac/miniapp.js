import { OS } from './dataType.js';
// tslint:disable-next-line:no-namespace
export var MiniApp;
(function (MiniApp) {
    var MiniAppImpl = /** @class */ (function () {
        function MiniAppImpl() {
        }
        MiniAppImpl.prototype.getOS = function () {
            if (window.navigator.userAgent.match(/Android/i)) {
                return OS.Android;
            }
            else if (window.navigator.userAgent.match(/iPhone|iPad|iPod/i)) {
                return OS.iOS;
            }
            else {
                return OS.Unknown;
            }
        };
        /* tslint:disable:no-any */
        MiniAppImpl.prototype.getUniqueId = function () {
            if (this.getOS() === OS.iOS) {
                return window.webkit.messageHandlers.MiniApp.getUniqueId();
            }
            else {
                return window.MiniApp.getUniqueId();
            }
        };
        return MiniAppImpl;
    }());
    MiniApp.MiniAppImpl = MiniAppImpl;
})(MiniApp || (MiniApp = {}));
//# sourceMappingURL=miniapp.js.map
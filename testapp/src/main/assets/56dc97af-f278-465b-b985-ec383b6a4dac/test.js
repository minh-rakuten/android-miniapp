import { MiniApp } from './miniapp.js';
function showUniqueId() {
    let instance = new MiniApp.MiniAppImpl();
    document.getElementById('version').innerHTML = instance.getUniqueId();
}
//# sourceMappingURL=test.js.map
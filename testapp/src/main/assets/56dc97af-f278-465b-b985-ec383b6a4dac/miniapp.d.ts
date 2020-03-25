import { OS } from './dataType.js';
export declare namespace MiniApp {
    interface MiniAppInterface {
        getUniqueId(): string;
    }
    class MiniAppImpl implements MiniAppInterface {
        getOS(): OS;
        getUniqueId(): string;
    }
}

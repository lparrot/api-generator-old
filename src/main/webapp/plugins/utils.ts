import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'

export default async (ctx: Context, inject: Inject) => {
  inject('utils', new Utils())
}

class Utils {
  pickAttributes (obj: any, attributes: string[]) {
    return Object.fromEntries(
      Object.entries(obj)
        .filter(([ key ]) => attributes.includes(key)),
    );
  }

  generateUUID () {
    let d = new Date().getTime()//Timestamp
    let d2 = (performance && performance.now && (performance.now() * 1000)) || 0//Time in microseconds since page-load or 0 if unsupported
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      let r = Math.random() * 16//random number between 0 and 16
      if (d > 0) { //Use timestamp until depleted
        r = (d + r) % 16 | 0;
        d = Math.floor(d / 16);
      } else { //Use microseconds since page-load if supported
        r = (d2 + r) % 16 | 0;
        d2 = Math.floor(d2 / 16);
      }
      return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
  }

  formatBytes (bytes, decimals = 2) {
    if (bytes === 0) return '0 Bytes';

    const k = 1024;
    const dm = decimals < 0 ? 0 : decimals;
    const sizes = [ 'Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB' ];

    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }
}

declare module 'vue/types/vue' {
  interface Vue {
    readonly $utils: Utils
  }
}

declare module '@nuxt/types' {
  interface NuxtAppOptions {
    readonly $utils: Utils
  }

  interface Context {
    readonly $utils: Utils
  }
}

declare module 'vuex' {
  interface Store<S> {
    readonly $utils: Utils
  }
}

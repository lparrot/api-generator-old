import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'
import dayjs from 'dayjs'

export default async (ctx: Context, inject: Inject) => {
  inject('dayjs', dayjs)
}

declare module 'vue/types/vue' {
  interface Vue {
    $dayjs: typeof dayjs
  }
}

declare module '@nuxt/types' {
  interface NuxtAppOptions {
    $dayjs: typeof dayjs
  }

  interface Context {
    $dayjs: typeof dayjs
  }
}

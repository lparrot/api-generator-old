import Fragment from 'vue-fragment'
import { Context } from '@nuxt/types'
import Vue from 'vue'

export default async (ctx: Context) => {
  Vue.use(Fragment.Plugin)
}

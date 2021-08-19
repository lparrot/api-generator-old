import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'
import get from 'lodash.get'

export default async (ctx: Context, inject: Inject) => {
  inject('expLang', new ExpressionLanguage())
}

class ExpressionLanguage {
  convert (element, expression) {
    expression = this.convertParameters(element, expression)
    expression = this.convertEval(element, expression)
    return expression
  }

  convertParameters (element, expression) {
    return expression.replaceAll(/(?:\$P\()(.+)(?:\))/ig, (exp, match1) => {
      return get(element, match1)
    })
  }

  convertEval (element, expression) {
    return expression.replaceAll(/(?:\$EVAL\()(.+)(?:\))/ig, (exp, match1) => {
      return eval(`function evalFunction (data) { return ${ match1 }};JSON.stringify(evalFunction(element))`)
    })
  }
}


declare module 'vue/types/vue' {
  interface Vue {
    readonly $expLang: ExpressionLanguage
  }
}

declare module '@nuxt/types' {
  interface NuxtAppOptions {
    readonly $expLang: ExpressionLanguage
  }

  interface Context {
    readonly $expLang: ExpressionLanguage
  }
}

declare module 'vuex' {
  interface Store<S> {
    readonly $expLang: ExpressionLanguage
  }
}

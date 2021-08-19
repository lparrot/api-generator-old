const theme = require('./themes/default')

export default {
  // Disable server-side rendering: https://go.nuxtjs.dev/ssr-mode
  ssr: false,

  // Target: https://go.nuxtjs.dev/config-target
  target: 'static',

  static: {
    // Aucun préfixe ne sera rajouté au niveau du baseUrl pour les images par exemple
    prefix: false,
  },

  generate: {
    dir: '../../../target/dist',
  },

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    titleTemplate: (titleChunk) => {
      return titleChunk ? titleChunk + ' - API Generator' : 'API Generator'
    },

    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { name: 'format-detection', content: 'telephone=no' },
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
    ],
  },

  /*
  ** Personnalisation de la barre de chargement
  */
  loading: false,

  loadingIndicator: {
    name: 'chasing-dots',
    color: '#3B8070',
    background: 'var(--light)',
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [
    '@fortawesome/fontawesome-free/css/all.css',
    '@/assets/styles/tailwind.scss',
  ],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [
    '@/plugins/vue-fragment',
    '@/plugins/axios',
    '@/plugins/utils',
    '@/plugins/date-fns',
    '@/plugins/chartjs',
    '@/plugins/websocket',
    '@/plugins/explang',
  ],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/typescript
    '@nuxt/typescript-build',
    // https://go.nuxtjs.dev/tailwindcss
    '@nuxtjs/tailwindcss',
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    // https://go.nuxtjs.dev/axios
    '@nuxtjs/axios',
    // https://go.nuxtjs.dev/pwa
    '@nuxtjs/pwa',
  ],

  // Axios module configuration: https://go.nuxtjs.dev/config-axios
  // Axios module configuration (https://go.nuxtjs.dev/config-axios)
  axios: {
    prefix: '/api',
    proxy: true,
  },

  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
    '/ws': {
      target: 'http://localhost:8080',
      changeOrigin: false,
      ws: true,
    },
  },

  tailwindcss: {
    cssPath: false,
    config: {
      mode: 'jit',
      theme,
      variants: {
        extend: {
          fontWeight: ['hover', 'focus'],
          opacity: ['disabled'],
          cursor: ['disabled'],
        },
      },
    },
  },

  // PWA module configuration: https://go.nuxtjs.dev/pwa
  pwa: {
    manifest: {
      lang: 'en',
    },
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    extractCSS: true,
  },
}

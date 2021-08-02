<template>
  <div class="relative">
    <button class="relative z-10 block w-8 h-8 overflow-hidden rounded-full shadow focus:outline-none" @click="dropdownOpen = !dropdownOpen">
      <img alt="Your avatar" class="object-cover w-full h-full" src="https://images.unsplash.com/photo-1528892952291-009c663ce843?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=296&q=80"/>
    </button>
    <div v-show="dropdownOpen" class="fixed inset-0 z-10 w-full h-full" @click="dropdownOpen = false"></div>

    <transition enter-active-class="transition duration-150 ease-out transform" enter-from-class="scale-95 opacity-0" enter-to-class="scale-100 opacity-100" leave-active-class="transition duration-150 ease-in transform" leave-from-class="scale-100 opacity-100" leave-to-class="scale-95 opacity-0">
      <div v-show="dropdownOpen" class="absolute right-0 z-20 w-48 py-2 mt-2 bg-white rounded-md shadow-xl">
        <a class="block px-4 py-2 text-sm text-gray-700 hover:bg-indigo-600 hover:text-white" href="#">Profile</a>
        <a class="block px-4 py-2 text-sm text-gray-700 hover:bg-indigo-600 hover:text-white" href="#">Products</a>
        <router-link class="block px-4 py-2 text-sm text-gray-700 hover:bg-indigo-600 hover:text-white" to="/">Log out
        </router-link>
      </div>
    </transition>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'

@Component
export default class Dropdown extends Vue {
  dropdownOpen: boolean = false
}
</script>

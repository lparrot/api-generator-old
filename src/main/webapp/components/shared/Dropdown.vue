<template>
  <div class="w-full h-full">
    <div class="relative">
      <button class="relative w-full border-gray-300 bg-white rounded-md border pl-3 px-2 py-2 text-left cursor-default focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" type="button" @click="dropdownOpen = !dropdownOpen">
        <div class="flex items-center inline">
          <slot name="content">
            No content
          </slot>
        </div>
        <span class="ml-3 absolute inset-y-0 right-0 flex items-center pr-2 pointer-events-none">
            <svg aria-hidden="true" class="h-5 w-5 text-gray-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                <path clip-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" fill-rule="evenodd"></path>
            </svg>
        </span>
      </button>
      <div v-if="dropdownOpen" class="absolute mt-1 w-full z-10 rounded-md bg-white shadow-lg">
        <ul aria-activedescendant="listbox-item-3" aria-labelledby="listbox-label" class="max-h-56 rounded-md py-1 text-base ring-1 ring-black ring-opacity-5 overflow-auto focus:outline-none sm:text-sm" role="listbox" tabindex="-1">
          <li id="listbox-item-0" class="text-gray-900 cursor-default hover:bg-indigo-500 hover:text-white select-none relative py-2 pl-3 pr-9" role="option">
            <div class="flex items-center">
              <img alt="person" class="flex-shrink-0 h-6 w-6 rounded-full" src="/images/person/1.jpg"/>
              <span class="ml-3 block font-normal truncate">
                            Mick Poulaz
                        </span>
            </div>
            <span class="absolute inset-y-0 right-0 flex items-center pr-4">
                        <svg aria-hidden="true" class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                            <path clip-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" fill-rule="evenodd">
                            </path>
                        </svg>
                    </span>
          </li>
          <li id="listbox-item-1" class="text-gray-900 cursor-default select-none hover:bg-indigo-500 hover:text-white relative py-2 pl-3 pr-9" role="option">
            <div class="flex items-center">
              <img alt="person" class="flex-shrink-0 h-6 w-6 rounded-full" src="/images/person/1.jpg"/>
              <span class="ml-3 block font-normal truncate">
                            Julien Schiano
                        </span>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, VModel, Vue } from 'nuxt-property-decorator'

@Component
export default class Dropdown extends Vue {
  @VModel({ type: Object }) model!: any

  dropdownOpen: boolean = false
}
</script>

<template>
  <div class="w-full">
    <div class="relative">
      <button :class="{'z-50': dropdownOpen}" class="relative w-full border-gray-300 bg-white h-10 rounded-md border pl-3 px-2 text-left cursor-pointer focus:outline-none focus:border-secondary-500 sm:text-sm" type="button" @click="dropdownOpen = !dropdownOpen" @keyup.down="open" @keyup.up="close">
        <div class="flex  items-center inline">
          <slot :model="model" name="content">
            {{ model }}
          </slot>
        </div>
        <span class="ml-3 absolute inset-y-0 right-0 flex items-center pr-2 pointer-events-none">
            <svg aria-hidden="true" class="h-5 w-5 text-gray-400" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                <path clip-rule="evenodd" d="M10 3a1 1 0 01.707.293l3 3a1 1 0 01-1.414 1.414L10 5.414 7.707 7.707a1 1 0 01-1.414-1.414l3-3A1 1 0 0110 3zm-3.707 9.293a1 1 0 011.414 0L10 14.586l2.293-2.293a1 1 0 011.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" fill-rule="evenodd"></path>
            </svg>
        </span>
      </button>
      <div v-if="dropdownOpen" class="absolute mt-1 w-full z-50 rounded-md bg-white shadow-lg">
        <ul v-on-clickaway="close" aria-labelledby="listbox-label" class="max-h-56 rounded-md py-1 text-base ring-1 ring-black ring-opacity-5 overflow-auto focus:outline-none sm:text-sm" role="listbox" tabindex="-1">
          <template v-for="(item, itemIndex) in items">
            <li :id="`dropdown-item-${itemIndex}`" :key="itemIndex" :class="{'bg-secondary-400 text-white': itemIndex === selectedIndex}" class="text-gray-900 hover:bg-secondary-500 hover:text-white select-none relative py-2 pl-3 pr-9 cursor-pointer focus:outline-black" role="option" tabindex="0" @click="onSelect(item, itemIndex)" @keyup.enter="onSelect(item, itemIndex)">
              <slot :item="item">
                {{ item }}
              </slot>
              <div v-if="itemIndex === selectedIndex" class="inline absolute inset-y-0 right-0 flex items-center pr-4">
                <svg aria-hidden="true" class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                  <path clip-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" fill-rule="evenodd"></path>
                </svg>
              </div>
            </li>
          </template>
        </ul>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, VModel, Vue } from 'nuxt-property-decorator'
import { mixin as clickaway } from 'vue-clickaway'

@Component({
  mixins: [ clickaway ],
})
export default class Dropdown extends Vue {
  @Prop({ type: Array, required: true }) items!: any[]
  @Prop() id!: string
  @VModel() model!: any

  dropdownOpen: boolean = false
  selectedIndex: number = null

  toggle () {
    this.dropdownOpen = !this.dropdownOpen
  }

  open () {
    this.dropdownOpen = true
  }

  close () {
    this.dropdownOpen = false
  }

  onSelect (item, itemIndex) {
    this.toggle()
    this.selectedIndex = itemIndex
    this.model = item
  }
}
</script>

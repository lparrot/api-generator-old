<template>
  <section v-if="d_opened">
    <div class="overflow-x-hidden overflow-y-auto fixed inset-0 z-50 outline-none focus:outline-none justify-center items-center flex">
      <div class="relative w-5/6 w-auto my-6 mx-auto">
        <!--content-->
        <div class="border-0 rounded-lg shadow-lg relative flex flex-col w-full bg-white outline-none focus:outline-none">
          <!--header-->
          <div class="flex items-start justify-between p-3 border-b border-solid border-blueGray-200 rounded-t">
            <h4 class="text-xl font-semibold">
              <slot name="title">{{ title }}</slot>
            </h4>
            <button class="p-1 ml-auto bg-transparent border-0 text-black float-right text-3xl leading-none font-semibold outline-none focus:outline-none" @click="toggle">
              <span class="text-black h-6 w-6 text-2xl block outline-none focus:outline-none">×</span>
            </button>
          </div>
          <!--body-->
          <div class="relative p-6 flex-auto">
            <slot></slot>
          </div>
          <!--footer-->
          <div class="flex items-center justify-end p-3 border-t border-solid border-blueGray-200 rounded-b">
            <slot name="footer">
              <button class="p-btn--danger" type="button" @click="toggle">
                Close
              </button>
              <button class="p-btn--success ml-4" type="button" @click="toggle">
                Ok
              </button>
            </slot>
          </div>
        </div>
      </div>
    </div>
    <div class="opacity-25 fixed inset-0 z-40 bg-black"></div>
  </section>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'nuxt-property-decorator'

@Component
export default class Modal extends Vue {
  @Prop({ type: Boolean, default: false }) opened!: boolean
  @Prop({ type: String }) title!: string

  d_opened = this.opened

  show () {
    this.d_opened = true
  }

  hide () {
    this.d_opened = false
  }

  toggle () {
    this.d_opened = !this.d_opened
  }
}
</script>

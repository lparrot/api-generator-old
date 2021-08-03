<template>
  <div>
    <h3 class="text-gray-700 text-3xl font-medium">{{ mode === 'add' ? 'Add new' : 'Edit' }} {{ content.name }} structure</h3>

    <form @submit.prevent="submit">
      <fieldset class="form-field required">
        <label for="input-name">Name</label>
        <input id="input-name" v-model="content.name" class="form-input" name="name" required>
      </fieldset>

      <div class="py-3">
        <hr>
      </div>

      <fieldset>
        <legend class="text-base font-medium text-gray-900 mb-2">Fields</legend>

        <table class="w-full table-auto">
          <thead>
            <tr>
              <th></th>
              <th></th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(field, fieldIndex) in content.contentFields" :key="fieldIndex" class="border-b">
              <td class="py-2 w-10">
                <i :class="getIcon(field)"></i>
              </td>
              <td>
                {{ field.name }}
              </td>
              <td></td>
              <td class="w-20">
                <i class="fas fa-pencil-alt p-2 rounded-full hover:bg-info-200 cursor-pointer" @click="editField(field)"></i>
                <i class="fas fa-trash mx-1 p-2 rounded-full hover:bg-danger-200 cursor-pointer"></i>
              </td>
            </tr>
          </tbody>
        </table>

        <button class="p-btn--success w-full mt-5" type="button" @click="addField">Add another field</button>
      </fieldset>
    </form>

    <modal ref="modalEditField" modal-class="w-2/3">
      <template #title>{{ fieldEdit.id == null ? 'Add new field' : `Edit field ${ content.name } / ${ fieldEdit.name }` }}</template>

      <form id="formEditField" @submit.prevent="submit">
        <div class="grid grid-cols-2 space-x-2">
          <fieldset class="form-field required">
            <label for="input-field-name">Name</label>
            <input id="input-field-name" v-model="fieldEdit.name" class="form-input" name="name" required>
          </fieldset>
          <fieldset class="form-field required">
            <label for="input-field-type">Type</label>
            <dropdown id="input-field-type"></dropdown>
          </fieldset>
        </div>
      </form>
    </modal>
  </div>
</template>

<script lang="ts">
import { Component, Ref, Vue } from 'nuxt-property-decorator'
import Modal from '~/components/shared/Modal.vue'
import Dropdown from '~/components/shared/Dropdown.vue'

declare type EditMode = 'add' | 'edit'

@Component({
  components: {
    Modal, Dropdown,
  },
})
export default class PageContentTypes extends Vue {
  @Ref('modalEditField') modalEditField: Modal

  content: any = { contentFields: [] }
  fieldEdit: any = {}

  get mode (): EditMode {
    return this.$route.params.id != null ? 'edit' : 'add'
  }

  async fetch () {
    if (this.mode === 'edit') {
      this.content = await this.$axios.$get(`/contents/${ this.$route.params.id }`)
    }
  }

  async submit () {
    if (this.mode === 'edit') {
      await this.$axios.$post(`/contents`, this.content)
    } else {
      await this.$axios.$put(`/contents/${ this.$route.params.id }`, this.content)
    }
  }

  addField () {
    this.fieldEdit = {}
    this.modalEditField.show()
  }

  editField (field) {
    this.fieldEdit = { ...field }
    this.modalEditField.show()
  }

  getIcon (field) {
    switch (field?.type?.code) {
      case 'STRING':
        return 'fas fa-font'
      case 'TEXT':
        return 'fas fa-align-left'
      case 'RICHTEXT':
        return 'fas fa-align-justify'
      case 'UID':
        return 'fas fa-tags'
      case 'NUMBER':
        return 'fas fa-sort-numeric-up'
      case 'DATE':
        return 'fas fa-calendar'
      case 'TIME':
        return 'fas fa-clock'
      case 'DATETIME':
        return 'fas fa-calendar-alt'
      case 'RELATION':
        return 'fas fa-sitemap'
      default:
        break
    }
  }
}
</script>

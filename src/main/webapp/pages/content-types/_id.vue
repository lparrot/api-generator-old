<template>
  <div>
    <h3 class="text-gray-700 text-3xl font-medium">{{ mode === 'add' ? 'Add new' : 'Edit ' + content.name }} structure</h3>

    <form @submit.prevent="submit">
      <fieldset class="form-field required">
        <label for="input-name">Name</label>
        <input id="input-name" v-model="content.name" class="form-input" name="name" required>
      </fieldset>

      <button class="p-btn--success" type="submit">Save data</button>
    </form>

    <template v-if="mode === 'edit'">
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
            <tr v-for="(field, fieldIndex) in fields" :key="fieldIndex" class="border-b hover:bg-primary-100">
              <td class="py-2 w-10">
                <i :class="getIcon(field.type)"></i>
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
    </template>

    <modal ref="modalEditField" modal-class="w-2/3">
      <template v-if="fieldEdit != null" #title>{{ fieldEdit.id == null ? 'Add new field' : `Edit field ${ content.name } / ${ fieldEdit.name }` }}</template>

      <form v-if="fieldEdit != null" id="formEditField" @submit.prevent="submitField">
        <div class="grid grid-cols-2 gap-2">
          <fieldset class="form-field required">
            <label for="input-field-name">Name</label>
            <input id="input-field-name" v-model="fieldEdit.name" class="form-input" name="name" required>
          </fieldset>
          <fieldset class="form-field required">
            <label>Type</label>
            <dropdown v-model="fieldEdit.type" :items="fieldTypes">
              <template #default="{item}">
                <i :class="getIcon(item)"></i>
                <span class="ml-4">{{ item.name }}</span>
              </template>

              <template #content="{model}">{{ model && model.name }}</template>
            </dropdown>
          </fieldset>
          <fieldset class="form-field flex items-center">
            <div class="grid grid-cols-1 gap-1">
              <label class="inline-flex items-center mt-3">
                <input v-model="fieldEdit.hideInList" checked class="form-checkbox h-5 w-5 text-secondary-600" type="checkbox">
                <span class="ml-2 label">Hide in list</span>
              </label>
              <label class="inline-flex items-center mt-3">
                <input v-model="fieldEdit.nullable" checked class="form-checkbox h-5 w-5 text-secondary-600" type="checkbox">
                <span class="ml-2 label">Nullable</span>
              </label>
            </div>
          </fieldset>
        </div>
      </form>

      <template v-if="fieldEdit != null" #footer="{hide}">
        <button class="p-btn--danger" type="button" @click="hide">Close</button>
        <button class="p-btn--success" form="formEditField" type="submit">Save</button>
      </template>
    </modal>
  </div>
</template>

<script lang="ts">
import { Action, Component, Ref, State, Vue } from 'nuxt-property-decorator'
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

  @State(state => state.lists.fieldtypes) fieldTypes
  @Action('lists/getFieldTypes') getFieldTypes
  @Action('findContents') findContents

  content: any = { contentFields: [] }
  fieldEdit: any = null

  get mode (): EditMode {
    return this.$route.params.id != null ? 'edit' : 'add'
  }

  get modeField (): EditMode {
    return this.fieldEdit.id != null ? 'edit' : 'add'
  }

  get fields () {
    return this.content.contentFields.filter(field => !field.primaryKey)
  }

  async fetch () {
    if (this.mode === 'edit') {
      this.content = await this.$axios.$get(`/contents/${ this.$route.params.id }`)
    }
  }

  async submit () {
    if (this.mode === 'edit') {
      this.content = await this.$axios.$put(`/contents/${ this.content.id }`, this.content)
    } else {
      const content = await this.$axios.$post(`/contents`, this.content)
      if (content !== '') {
        await this.$router.push(`/content-types/${ content.id }`)
      }
    }
    await this.findContents()
  }

  async submitField () {
    let field

    this.fieldEdit.type = this.fieldEdit.type.code

    if (this.modeField === 'edit') {
      field = await this.$axios.$put(`/contents/${ this.content.id }/fields/${ this.fieldEdit.id }`, this.$utils.pickAttributes(this.fieldEdit, [ 'name', 'type', 'hideInList', 'nullable' ]))
      this.$set(this.content.contentFields, this.content.contentFields.findIndex(field => field.dbFieldName === this.fieldEdit.dbFieldName), field)
    } else {
      field = await this.$axios.$post(`/contents/${ this.content.id }/fields`, this.fieldEdit)
      if (field !== '') {
        this.content.contentFields.push(field)
      }
    }
    this.fieldEdit = null
    this.modalEditField.hide()
  }

  async addField () {
    this.fieldEdit = { hideInList: false, nullable: true }
    await this.getFieldTypes()
    this.modalEditField.show()
  }

  async editField (field) {
    this.fieldEdit = { ...field }
    await this.getFieldTypes()
    this.modalEditField.show()
  }

  getIcon (type) {
    switch (type?.code) {
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

import { ActionTree, GetterTree, MutationTree } from 'vuex';

export const state = () => ({
  sidebar: false,
  contents: [],
})

export type RootState = ReturnType<typeof state>

export const getters: GetterTree<RootState, RootState> = {}

export const mutations: MutationTree<RootState> = {
  SET_SIDEBAR_STATE (state, sidebarState) {
    state.sidebar = sidebarState
  },
  SET_CONTENTS (state, contents) {
    state.contents = contents
  },
}

export const actions: ActionTree<RootState, RootState> = {
  updateSidebar ({ commit }, sidebarState) {
    commit('SET_SIDEBAR_STATE', sidebarState)
  },

  async findContents ({ commit }) {
    const contents = await this.$axios.$get('/contents')
    commit('SET_CONTENTS', contents)
  },
}

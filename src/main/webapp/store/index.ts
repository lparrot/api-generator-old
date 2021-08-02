import { ActionTree, GetterTree, MutationTree } from 'vuex';

export const state = () => ({
  sidebar: false,
})

export type RootState = ReturnType<typeof state>

export const getters: GetterTree<RootState, RootState> = {}

export const mutations: MutationTree<RootState> = {
  SET_SIDEBAR_STATE (state, sidebarState) {
    state.sidebar = sidebarState
  },
}

export const actions: ActionTree<RootState, RootState> = {
  updateSidebar ({ commit }, sidebarState) {
    commit('SET_SIDEBAR_STATE', sidebarState)
  },
}

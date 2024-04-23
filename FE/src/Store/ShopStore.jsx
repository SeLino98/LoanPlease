import { create } from 'zustand';

const ShopStore = create((set) => ({
  currentComponent: 'gameItem',
  setCurrentComponent: (component) => set({ currentComponent: component }),

  isModalOpen: false,
  openModal: (content) => set({ isModalOpen: true, modalContent: content }),
  closeModal: () => set({ isModalOpen: false, modalContent: null }),
}));


export default ShopStore;
import React from "react";
import ShopStore from "../../Store/ShopStore";
import GameItem from "./GameItem";
import LoanItem from "./LoanItem";
import ShopModal from "../Modal/ShopModal";

function Shop() {
  const setCurrentComponent = ShopStore((state) => state.setCurrentComponent);
  const currentComponent = ShopStore((state) => state.currentComponent);
  const isModalOpen = ShopStore((state) => state.isModalOpen);
  const openModal = ShopStore((state) => state.openModal);
  const closeModal = ShopStore((state) => state.closeModal);

  let currentPage;
  if (currentComponent === "gameItem") {
    currentPage = <GameItem openModal={openModal} />;
  } else {
    currentPage = <LoanItem openModal={openModal} />;
  }

  return (
    <>
      {isModalOpen && (
        <ShopModal closeModal={closeModal} />
      )}
      <div className="bg-cusColor3 min-h-screen w-full overflow-auto grid grid-cols-5">
        <div className="col-span-1 border-2 flex flex-col justify-between text-lg font-cusFont1 flex-grow-0">
          <div className="grid grid-rows mx-2 my-3">
            <button
              className="border-2 my-1 py-2 bg-orange-400 hover:bg-orange-600 rounded-lg"
              onClick={() => setCurrentComponent("gameItem")}
            >
              게임진행
            </button>
            <button
              className="border-2 my-1 py-2 bg-orange-400 hover:bg-orange-600 rounded-lg"
              onClick={() => setCurrentComponent("loanItem")}
            >
              대출상품
            </button>
          </div>
          <button
            className="text-white border-2 mx-2 my-3 bg-blue-800 hover:bg-blue-900 py-2 rounded-lg"
          >
            돌아가기
          </button>
        </div>
        <div className="col-span-4 border-2 grid grid-rows-10">
          <div className="row-span-1 border-2 flex justify-end items-center">
            <div className="border-2 rounded-lg bg-white text-center font-cusFont1 text-lg mx-6 pl-24 pr-2 py-2">
              1000
            </div>
          </div>
          <div className="row-span-9 border-2">
            {currentPage}
          </div>
        </div>
      </div>
    </>
  );
}

export default Shop;

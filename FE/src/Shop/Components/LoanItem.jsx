import React from "react";

function LoanItem({ openModal }) {
  return (
    <div className="flex justify-center gap-4 my-2 text-center w-full">
      <div className="flex-grow-1 border-2 bg-white px-6 py-4 rounded-lg">
        <p className="font-cusFont1 my-2">A대출</p>
        <p className="font-cusFont2 my-2">금리가 어쩌고 설명</p>
        <button 
          className="font-cusFont1 my-2 bg-orange-400 hover:bg-orange-600 border-2 rounded-lg px-3 py-2"
          onClick={() => openModal}
        >
          가격
        </button>
      </div>
      <div className="flex-grow-1 border-2 bg-white px-6 py-4 rounded-lg">
        <p className="font-cusFont1 my-2">B상품</p>
        <p className="font-cusFont2 my-2">설명</p>
        <button 
          className="font-cusFont1 my-2 bg-orange-400 hover:bg-orange-600 border-2 rounded-lg px-3 py-2"
          onClick={() => openModal}
        >
          가격
        </button>
      </div>
      <div className="flex-grow-1 border-2 bg-white px-6 py-4 rounded-lg">
        <p className="font-cusFont1 my-2">C상품</p>
        <p className="font-cusFont2 my-2">설명</p>
        <button 
          className="font-cusFont1 my-2 bg-orange-400 hover:bg-orange-600 border-2 rounded-lg px-3 py-2"
          onClick={() => openModal}
        >
          가격
        </button>
      </div>
    </div>
  )
}

export default LoanItem;

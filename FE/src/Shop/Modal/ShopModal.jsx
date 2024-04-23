function ShopModal({ closeModal }) {
  return(
    <>
      <div className="bg-yellow-200">
        <p>모달</p>
        <div>
          <button>ㄱ</button>
          <button onClick={closeModal}>ㄴ</button>
        </div>
      </div>
    </>
  )
}

export default ShopModal;
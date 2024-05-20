/* eslint-disable react/jsx-key */
import { useState } from "react";
import { PageQuery } from "./InfiniteQuery.jsx";
import { RankingStore } from "../Store.jsx";

const searchpagestyleClass = `
flex flex-col justify-between 
w-full h-[220px] my-2.5
`;

const searchlinestyleClass = `
w-full h-[10%] 
text-xl text-cusFont2 
outline-none border-b-2 mb-2.5 
`;

const searchresultareaClass = `
w-full h-[200px] 
overflow-x-hidden overflow-y-auto 
`;

// const searchpagestyle = {
//   width: "100%",
//   height: "220px",
//   display: "flex",
//   justifyContent: "space-between",
//   flexDirection: "column",
//   margin: "10px 0px",
// };

// const searchlinestyle = {
//   width: "100%",
//   height: "10%",
//   outline: "none",
//   fontSize: "20px",
//   fontFamily: "Orbit",
//   borderBottom: "2px solid",
//   marginBottom: "10px",
// };

// const searchresultarea = {
//   width: "100%",
//   height: "200px",
//   overflowX: "hidden",
//   overflowY: "auto",
// };

function SearchPage() {
  const inputdata = RankingStore((state) => state.inputdata);
  const setInputdata = RankingStore((state) => state.setInputdata);

  return (
    // <div style={searchpagestyle}>
    <div className={searchpagestyleClass}>
      {/* <input style={searchlinestyle} /> */}
      <input
        className={searchlinestyleClass}
        value={inputdata}
        onChange={(e) => {
          setInputdata(e.target.value);
        }}
      />
      {/* <div style={searchresultarea}> */}
      <div className={searchresultareaClass}>
        <PageQuery />
      </div>
    </div>
  );
}

export default SearchPage;

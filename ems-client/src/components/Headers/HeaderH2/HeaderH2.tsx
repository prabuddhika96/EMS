import React from "react";
import "./style.css";

interface Props {
  text: string;
}
function HeaderH2({ text }: Props) {
  return <h2 className="ems-header-2">{text}</h2>;
}

export default HeaderH2;

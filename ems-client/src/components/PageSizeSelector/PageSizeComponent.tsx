import React from "react";
import type { IOption } from "../../interface/Option";
import "./style.css";
import PageSelector from "./PageSelector";

interface Props {
  totalRecords: number;
  handleChangePageSize: any;
}

function PageSizeComponent({ totalRecords, handleChangePageSize }: Props) {
  return (
    <>
      {totalRecords > 0 && (
        <div className="ems-pagination-wrapper">
          <p className="ems-page-size-label">Page Size :</p>
          <PageSelector options={pageOptions} onChange={handleChangePageSize} />
        </div>
      )}
    </>
  );
}

export default PageSizeComponent;

export const pageOptions: IOption[] = [
  {
    group: "page-size",
    items: [
      { label: 10, value: "10" },
      { label: 15, value: "15" },
      { label: 20, value: "20" },
      { label: 25, value: "25" },
      { label: 30, value: "30" },
    ],
  },
];

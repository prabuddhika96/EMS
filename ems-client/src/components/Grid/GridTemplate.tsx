import React, { type ReactNode } from "react";
import PaginationComponent from "../Pagination/PaginationComponent";
import "./style.css";
import PageSizeComponent from "../PageSizeSelector/PageSizeComponent";

interface Props {
  children: ReactNode;
  totalRecords?: number;
  totalPages?: number;
  page?: number;
  handlePageChange?: any;
  handleChangePageSize?: any;
  ignoreHeight?: boolean;
}
function GridTemplate({
  children,
  totalPages,
  page,
  handlePageChange,
  totalRecords,
  handleChangePageSize,
  ignoreHeight = false,
}: Props) {
  return (
    <div>
      <div className={`${!ignoreHeight && `grid-container`}`}>
        <div
          className={`dashboard-grid ${
            !ignoreHeight && `dashboard-grid-max-height`
          }`}
        >
          {children}
        </div>
      </div>

      <div className="pagination-row">
        {totalPages && page && handlePageChange && (
          <PaginationComponent
            totalPages={totalPages}
            currentPage={page}
            onPageChnage={handlePageChange}
          />
        )}

        {totalRecords && handleChangePageSize && (
          <PageSizeComponent
            totalRecords={totalRecords}
            handleChangePageSize={handleChangePageSize}
          />
        )}
      </div>
    </div>
  );
}

export default GridTemplate;

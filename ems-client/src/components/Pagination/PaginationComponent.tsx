import { ChevronLeft, ChevronRight } from "lucide-react";
import React, { useState } from "react";
import "./style.css";

interface PaginationComponentProps {
  totalPages: number;
  currentPage: number;
  onPageChnage: (val: number) => void;
  showCount?: number;
}

function PaginationComponent({
  totalPages,
  currentPage,
  onPageChnage,
  showCount,
}: PaginationComponentProps) {
  const [pagesToShow, setPagesToShow] = useState<number>(showCount ?? 1);

  const pageNumbers = [];

  if (totalPages <= pagesToShow * 2 + 2) {
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }
  } else if (currentPage <= pagesToShow + 2) {
    for (
      let i = 1;
      i <= pagesToShow * 2 + (pagesToShow % 2 === 0 ? 1 : 2) + 1;
      i++
    ) {
      pageNumbers.push(i);
    }
    if (pageNumbers.length !== totalPages) {
      if (pageNumbers.length !== totalPages - 1) {
        pageNumbers.push("...");
      }
      pageNumbers.push(totalPages);
    }
  } else if (currentPage >= totalPages - pagesToShow - 1) {
    for (let i = totalPages - (pagesToShow * 2 + 1) - 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }
    if (pageNumbers.length !== totalPages) {
      if (pageNumbers.length !== totalPages - 1) {
        pageNumbers.unshift("...");
      }
      pageNumbers.unshift(1);
    }
  } else {
    pageNumbers.push(1);
    pageNumbers.push("...");
    for (
      let i = currentPage - pagesToShow;
      i <= currentPage + pagesToShow;
      i++
    ) {
      pageNumbers.push(i);
    }
    pageNumbers.push("...");
    pageNumbers.push(totalPages);
  }

  return (
    <>
      {totalPages > 0 && (
        <div className="pagination-wrapper">
          <ChevronLeft
            className={`pagination-icon ${
              currentPage === 1 ? "disabled" : "enabled"
            }`}
            size={22}
            onClick={() => {
              if (currentPage === 1) return;
              onPageChnage(Math.max(1, currentPage - 1));
            }}
          />
          {pageNumbers.map((pageNumber, index) => (
            <button
              key={index}
              disabled={pageNumber === "..."}
              onClick={() => {
                if (typeof pageNumber === "number") {
                  onPageChnage(pageNumber);
                }
              }}
              className={`pagination-button ${
                pageNumber === currentPage ? "current" : "enabled"
              }`}
            >
              {pageNumber}
            </button>
          ))}
          <ChevronRight
            className={`pagination-icon ${
              currentPage === totalPages ? "disabled" : "enabled"
            }`}
            size={22}
            onClick={() => {
              if (currentPage === totalPages) return;
              onPageChnage(Math.min(totalPages, currentPage + 1));
            }}
          />
        </div>
      )}
    </>
  );
}

export default PaginationComponent;

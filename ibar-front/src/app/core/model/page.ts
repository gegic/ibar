class Pageable {
  pageNumber: number;
}

export class Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  pageable: Pageable;
}

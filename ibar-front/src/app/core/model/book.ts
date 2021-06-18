import {Author} from './author';

export class Book {
  id?: string;
  name?: string;
  description?: string;
  categoryId?: string;
  categoryName?: string;
  authorNames?: string[];
  cover?: string;
  numReviews?: number;
  averageRating?: number;
  inReadingList?: boolean;
  numRead?: number;
  pdf?: string;
  quantity?: number;
  authorIds?: string[];
}

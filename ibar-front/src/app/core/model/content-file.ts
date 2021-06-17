export class ContentFile {
  path?: string;
  quantity?: number;

  constructor(path?: string, quantity?: number) {
    this.path = path;
    this.quantity = quantity;
  }
}

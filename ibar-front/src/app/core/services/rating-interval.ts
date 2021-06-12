export class RatingInterval {
  min: number;
  max: number;
  interval = [0, 5];

  public applyInterval(): RatingInterval {
    this.min = this.interval[0];
    this.max = this.interval[1];
    return this;
  }
}

export class RatingInterval {
  min = 0;
  max = 5;
  interval = [0, 5];

  public applyInterval(): RatingInterval {
    this.min = this.interval[0];
    this.max = this.interval[1];
    return this;
  }
}

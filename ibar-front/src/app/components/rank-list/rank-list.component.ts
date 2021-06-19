import {Component, OnInit} from '@angular/core';
import {RankService} from '../../core/services/rank.service';
import {Rank} from '../../core/model/rank';
import {ConfirmationService, MessageService} from 'primeng/api';

@Component({
  selector: 'app-rank-list',
  templateUrl: './rank-list.component.html',
  styleUrls: ['./rank-list.component.scss']
})
export class RankListComponent implements OnInit {

  rankDialog = false;
  ranks: Rank[] = [];
  rank?: Rank;
  submitted = false;

  constructor(private rankService: RankService,
              private confirmationService: ConfirmationService,
              private messageService: MessageService) { }

  ngOnInit(): void {
    this.getRanks();
  }

  getRanks(): void {
    this.rankService.getRanks().subscribe(
      (val: Rank[]) => {
        this.ranks = val;
      }
    );
  }

  openNew(): void {
    this.rank = undefined;
    this.submitted = false;
    this.rankDialog = true;
  }

  editRank(rank: Rank): void {
    this.rank = {...rank};
    this.rankDialog = true;
  }

  deleteRank(rank: Rank): void {
    this.confirmationService.confirm({
      message: 'Are you sure you want to delete ' + rank.name + '?',
      header: 'Confirm',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.rankService.delete(rank?.id ?? '').subscribe(
          () => {
            this.getRanks();
          }
        );
        this.rank = undefined;
        this.messageService.add({severity: 'success', summary: 'Successful', detail: 'Rank Deleted', life: 3000});
      }
    });
  }


  hideDialog(): void {
    this.rankDialog = false;
    this.submitted = false;
  }

  saveRank(): void {
    this.submitted = true;

    if (this.rank?.name?.trim()) {
      if (this.rank.id) {
        this.rankService.update(this.rank).subscribe(
          () => {
            this.rank = undefined;
            this.getRanks();
            this.messageService.add({severity: 'success', summary: 'Successful', detail: 'Rank Updated', life: 3000});
          }
        );
      }
      else {
        this.rankService.create(this.rank).subscribe(
          () => {
            this.getRanks();
            this.rank = undefined;
            this.messageService.add({severity: 'success', summary: 'Successful', detail: 'Rank Created', life: 3000});
          }
        );
      }

      this.rankDialog = false;
    }
  }

}

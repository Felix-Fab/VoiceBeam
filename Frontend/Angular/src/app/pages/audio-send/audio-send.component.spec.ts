import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AudioSendComponent } from './audio-send.component';

describe('AudioSendComponent', () => {
  let component: AudioSendComponent;
  let fixture: ComponentFixture<AudioSendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AudioSendComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AudioSendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

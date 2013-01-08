AnimalsSoundscapeElement : SoundscapeElement {
  var <>animalBufs;

  init {
    arg args;

    super.init(args);

    this.animalBufs = [\warblerBuf, \gullsBuf];
    this.transitionTime = 2.0;
    
    this.offTimeMin = 10.0;
    this.offTimeMax = 20.0;

    this.onTimeMin = 5.0;
    this.onTimeMax = 12.0;
  
  }

  prepare_for_play {

    super.prepare_for_play();

    this.instr = Patch("cs.sfx.PlayBuf", (
      buf: this.soundscape.bufs[this.animalBufs.choose()],
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime
    ));

    this.outChannel.play(this.instr);
  
  }

  play {

    super.play();

  
  }

}

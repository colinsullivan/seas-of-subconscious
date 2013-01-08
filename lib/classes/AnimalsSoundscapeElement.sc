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

  create_next_patch {

    super.create_next_patch();

    ^Patch("cs.sfx.PlayBuf", (
      buf: this.soundscape.bufs[this.animalBufs.choose()],
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime
    ));
  }

  play {

    super.play();

  
  }

}

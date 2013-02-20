DreamsSoundscapeElement : SoundscapeElement {

  var <>bufNames,
    <>bufSections,
    <>filterPatch;

  init {
    arg args;

    super.init(args);

    // filter voice
    this.filterPatch = FxPatch("cs.fx.HPFerModulated", (
      numChan: 2,
      cutoffMinFreq: 800,
      cutoffMaxFreq: 8000,
      cutoffModFreq: 0.3
    ));


    // send voice through reverb
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -5.0.dbamp()
    );

    this.outChannel.level = -8.0.dbamp();

    this.bufNames = [\derbyshireRunningBuf];

    this.bufSections = (
      derbyshireRunningBuf: [
        [65.5, 68.2],
        [68, 70],
        [87.2, 89.8],
        [91.8, 104.1],
        [104.1, 106.3],
        [113.2, 115.9],
        [117.9, 121.2],
        [121.7, 124.6]
      ]
    );

    this.transitionTime = 0.5;

    this.offTimeMin = 10.0;
    this.offTimeMax = 20.0;
  }

  create_next_patch {
    var bufKey,
      bufSection;

    bufKey = this.bufNames.choose();
    bufSection = this.bufSections[bufKey].choose();

    ("preparing dream: " ++ bufKey ++ " - " ++ bufSection).postln();

    this.onTimeMin = bufSection[1] - bufSection[0];
    this.onTimeMax = this.onTimeMin;

    ^Patch("cs.sfx.PlayBuf", (
      buf: this.soundscape.bufs[bufKey],
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      startTime: bufSection[0],
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime
    ));
  }

  play {
    super.play();

    this.outChannel.playfx(this.filterPatch);
  }

}

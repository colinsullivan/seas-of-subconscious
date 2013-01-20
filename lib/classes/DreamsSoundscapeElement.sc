DreamsSoundscapeElement : SoundscapeElement {

  var <>bufNames,
    <>bufSections;

  init {
    arg args;

    super.init(args);

    // send voice through reverb
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -5.0.dbamp()
    );

    this.outChannel.level = -5.0.dbamp();

    this.bufNames = [\derbyshireRunningBuf];

    this.bufSections = (
      derbyshireRunningBuf: [
        [65.5, 68.2],
        [68, 70],
        [87.2, 89.8],
        [91.8, 104.1]
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

    this.onTimeMin = this.onTimeMax = bufSection[1] - bufSection[0];

    ^Patch("DerbyshireFiltered", (
      buf: this.soundscape.bufs[bufKey],
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      startTime: bufSection[0],
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime
    ));
  }

}

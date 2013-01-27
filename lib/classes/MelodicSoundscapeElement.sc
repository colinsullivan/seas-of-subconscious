MelodicSoundscapeElement : SoundscapeElement {
  var <>soundscapeBufKeys;

  init {
    arg args;
    super.init(args);

    // reverb pretty wet
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -7.0.dbamp()
    );

    // far less dry signal
    this.outChannel.level = -13.0.dbamp();

    this.soundscapeBufKeys = [
      \avadhuta01Buf,
      \avadhuta02Buf,
      \descendingDissonantBuf,
      \triadsBuf
    ];

    this.transitionTime = 0.1;
    
    /*this.offTimeMin = 25.0;*/
    /*this.offTimeMax = 60.0;*/
    this.offTimeMin = 5.0;
    this.offTimeMax = 10.0;
  }

  create_next_patch {
    var bufKey,
      buf;

    super.create_next_patch();

    bufKey = this.soundscapeBufKeys.choose();
    buf = this.soundscape.bufs[bufKey];

    this.onTimeMin = buf.duration;
    this.onTimeMax = buf.duration;

    ("preparing melody: " ++ bufKey).postln();

    ^Patch("BassAutoGarble", (
      buf: buf,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime,
      convertToStereo: 1
    ));
  }
}

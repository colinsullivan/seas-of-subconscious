/**
 *  @file       init.sc 
 *
 *              Top-level initialization for the "Seas of the Subconscious"
 *              soundscape.
 *
 *  @author     Colin Sullivan <colinsul [at] gmail.com>
 *
 *              Copyright (c) 2012 Colin Sullivan
 *              Licensed under the GPLv3 license.
 **/

({
  var m,
    mBounds,
    projRoot = "/Users/colin/Projects/seas-of-subconscious",
    projSfx = projRoot +/+ "raw-samples";

  s.quit;
  
  /*s.options.inDevice = "PreSonus FIREPOD (2112)";*/
  /*s.options.inDevice = "SF + 1818";*/
  /*s.options.inDevice = "AudioBox 1818 VSL ";*/
  /*s.options.inDevice = "Soundflower (64ch)";*/

  s.options.memSize = 262144; // 256 Mb
  /*s.options.outDevice = "Soundflower (64ch)";*/
  s.options.sampleRate = 48000;
  s.options.blockSize = 8;
  s.boot();
  m = s.meter();

  // move level meter to bottom right of screen
  mBounds = m.window.bounds;
  /*mBounds.left = 1680;
  mBounds.top = 1000;*/
  mBounds.left = 1440;
  mBounds.top = 900;
  
  m.window.setTopLeftBounds(mBounds);

  Instr.dir = projRoot +/+ "lib/";
  Instr.loadAll();

  s.doWhenBooted({
    var soundsOfSubConsciousSeas, bufsToLoad;

    soundsOfSubConsciousSeas = SoundsOfSubconsciousSeas.new();
    soundsOfSubConsciousSeas.init();

    bufsToLoad = [
      ["58411__sinatra314__shorewaves1004.wav", \splashingWaterBuf],
      ["40563__genghis-attenborough__sedge-warbler-call.wav", \warblerBuf],
      ["59492__dobroide__20080807-seagulls.wav", \gullsBuf],
      ["101381__benboncan__creaking-floorboard.wav", \creakingFloorboardBuf],
      ["163300__laurent__canadian-loons-in-family.wav", \loonsBuf],
      ["32304__acclivity__shipsbell.wav", \shipsBellBuf],
      ["Inventions For Radio - Dreams" +/+ "02 The Dreams - 1. Running-filtered.aif", \derbyshireRunningBuf],
      ["melodic-bass" +/+ "avadhuta-01.aif", \avadhuta01Buf],
      ["melodic-bass" +/+ "avadhuta-02.aif", \avadhuta02Buf],
      ["melodic-bass" +/+ "descending-dissonant-chord.aif", \descendingDissonantBuf],
      ["melodic-bass" +/+ "triads.aif", \triadsBuf]
    ];

    bufsToLoad.do({
      arg bufData;

      var bufFileName = bufData[0],
        bufKey = bufData[1];

      soundsOfSubConsciousSeas.load_buf(projSfx +/+ bufFileName, bufKey);

    });
  });
  
}.value();)

Quarks.gui;

